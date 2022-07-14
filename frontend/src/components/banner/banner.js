import React, {Component} from 'react'
import Select from "react-select";
import "../styles/style.css"
import {Navigate, useNavigate} from "react-router";

const BASE_ENDPOINT = "http://localhost:8080"

export default class Banner extends Component {
    state = {
        id: null,
        title: "Create a new banner",
        name: "",
        price: "",
        categories: [],
        text: "",
        banners: [],
        categoryOptions: [],
        isCreate: true,
        error: undefined
    }

    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
        this.handleChooseCategory = this.handleChooseCategory.bind(this)
        this.onSave = this.onSave.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.clearForm = this.clearForm.bind(this);

    }

    handleChange(event) {
        const target = event.target;
        const name = target.name;
        this.setState({[name]: target.value});
    }

    handleChooseCategory = selectedOptions => {
        this.setState({categories: selectedOptions})
    }

    async getCategoryOptions() {
        const response = await fetch("http://localhost:8080/category/search?name=", {
            method: "GET",
            mode: "cors",
            headers: {
                "Authorization": sessionStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
            redirect: "follow",
            referrer: "no-referrer",
        });

        const data = await response.json();
        const options = data.map((d) => ({
            "value": d.id,
            "label": d.name
        }))
        this.setState({categoryOptions: options})
    }

    async getBanner(id) {
        this.hideError()
        const response = await fetch(BASE_ENDPOINT + "/banner/get?id=" + id, {
            method: "GET",
            mode: "cors",
            headers: {
                "Authorization": sessionStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
            redirect: "follow",
            referrer: "no-referrer",
        });

        const data = await response.json();

        const bannerCategories = data.categories.map((c) => ({
            "value": c.id,
            "label": c.name
        }))
        this.setState({name: data.name})
        this.setState({price: data.price})
        this.setState({categories: bannerCategories})
        this.setState({text: data.text})
        this.setState({id: data.id})
        this.setState({title: "Banner ID:" + data.id})
        this.setState({isCreate: false})
    }


    async searchBanners(bannerNamePart = "") {
        const response = await fetch(BASE_ENDPOINT + "/banner/search?name=" + bannerNamePart, {
            method: "GET",
            mode: "cors",
            headers: {
                "Authorization": sessionStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
            redirect: "follow",
            referrer: "no-referrer",
        });
        const data = await response.json();
        this.setState({banners: data})
    }

    async onDelete() {
        this.hideError()
        const endpoint = BASE_ENDPOINT + "/banner/delete?id=" + this.state.id
        const response = await fetch(endpoint, {
            method: "POST",
            mode: "cors",
            headers: {
                "Authorization": sessionStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
            redirect: "follow",
            referrer: "no-referrer",
            body: JSON.stringify({
                name: this.state.name,
                price: this.state.price,
                categoriesIDs: this.state.categories.map((c) => c.value),
                text: this.state.text
            })
        })

        if (!response.ok) {
            this.setState({error: {message: "Deleting problem"}})
            return
        }
        this.deleteBannerFromBannerSearchResults()
        this.clearForm()

    }

    async onSave() {
        this.hideError()
        if(!this.checkFormInput()) return
        const endpoint = this.state.isCreate ? BASE_ENDPOINT + "/banner/create" :
            BASE_ENDPOINT + "/banner/update?id=" + this.state.id

        const response = await fetch(endpoint, {
            method: "POST",
            mode: "cors",
            headers: {
                "Authorization": sessionStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
            redirect: "follow",
            referrer: "no-referrer",
            body: JSON.stringify({
                name: this.state.name,
                price: this.state.price,
                categoriesIDs: this.state.categories.map((c) => c.value),
                text: this.state.text
            })
        })

        if (!response.ok) {
            this.setState({error: {message: "Banner already exists"}})
            return
        }

        await this.searchBanners("")
        this.clearForm()
    }


    clearForm() {
        this.hideError()
        this.setState({title: "Create a new banner"})
        this.setState({id: null, name: "", price: "", text: "", categories: [], isCreate: true, error: undefined});
    }

    hideError() {
        this.setState({["error"]: undefined})
    }

    deleteBannerFromBannerSearchResults() {
        for (let i = 0; i < this.state.banners.length; i++) {
            if (this.state.banners[i].id === this.state.id) {
                this.state.banners.splice(i, 1)
            }
        }
    }

    checkFormInput() {
        try {
            if(this.state.name === "") throw Error("Invalid name")
            if(isNaN(this.state.price)) throw Error("Invalid price")
            if(this.state.text === "" ) throw Error("Text is empty")
            if(this.state.categories.length === 0) throw Error("Choose categories:")
            return true;
        }
        catch (err) {
            this.setState({error: {message: err.message}})
            return false;
        }
    }


    componentDidMount() {
        this.getCategoryOptions()
    }

    render() {
        return (
            <div className="container">
                {sessionStorage.getItem("jwt") === null && <Navigate to="/sign_in" replace={true}/>}
                <header className="header">
                    <nav className="header__nav">
                        <a href="/banner" className="header__link header__link_active">Banners</a>
                        {/* eslint-disable-next-line jsx-a11y/anchor-is-valid */}
                        <a href="/category" className="header__link">Categories</a>
                    </nav>
                </header>

                <main className="main">
                    <aside className="sidebar">
                        <header className="sidebar__header">Banners:</header>

                        <section className="sidebar__content">
                            <div className="sidebar__search">
                                <input className="sidebar__search-input" type="text"
                                       placeholder="Enter banner name..."
                                       onChange={(event) => this.searchBanners(event.target.value)}
                                />
                                <span className="sidebar__search-icon"></span>
                            </div>
                            <div className="sidebar__menu">
                                {/*eslint-disable-next-line jsx-a11y/anchor-is-valid*/}
                                {this.state.banners !== undefined && this.state.banners.map(item => {
                                    return (<a href="#"
                                               className="sidebar__menu-item"
                                               onClick={() => this.getBanner(item.id)}>
                                        {item.name}
                                    </a>);
                                })}
                            </div>
                        </section>

                        <footer className="sidebar__footer">
                            <button className="sidebar__submit-button" onClick={this.clearForm}>Create new banner
                            </button>
                        </footer>
                    </aside>

                    <section className="content">
                        <header className="content__header">
                            <span className="content__header-text">{this.state.title}</span>
                        </header>

                        <div className="content__body">
                            <div className="content__form">
                                <div className="content__form-item">
                                    <div className="content__form-item-title">Name</div>
                                    <div className="content__form-item-content">
                                        <input className="content__input" onChange={this.handleChange} type="text"
                                               name="name" value={this.state.name}/>
                                    </div>
                                </div>
                                <div className="content__form-item">
                                    <div className="content__form-item-title">Price</div>
                                    <div className="content__form-item-content">
                                        <input className="content__input" type="text" name="price"
                                               onChange={this.handleChange} value={this.state.price}/>
                                    </div>
                                </div>
                                <div className="content__form-item">
                                    <div className="content__form-item-title">Category</div>
                                    <div className="content__form-item-content">
                                        <Select options={this.state.categoryOptions} value={this.state.categories}
                                                isMulti={true} onChange={this.handleChooseCategory}></Select>

                                    </div>
                                </div>
                                <div className="content__form-item">
                                    <div className="content__form-item-title">Text</div>
                                    <div className="content__form-item-content">
                                    <textarea className="content__textarea" onChange={this.handleChange} name="text"
                                              value={this.state.text}>
                                    </textarea>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <footer className="content__footer">
                            <div className="content__buttons">
                                <button className="content__button content__button_dark" onClick={this.onSave}>Save
                                </button>
                                <button className="content__button content__button_red" onClick={this.onDelete}>Delete
                                </button>
                            </div>
                        </footer>
                        {this.state.error !== undefined ?
                            <div className="error">
                                <span className="error__text">{this.state.error.message}</span>
                            </div>
                            : null

                        }
                    </section>
                </main>
            </div>

        )
    }
}