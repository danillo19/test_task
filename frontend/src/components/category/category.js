import React, {Component} from 'react'
import Select from "react-select";
import "../styles/style.css"
import {Navigate} from "react-router";

const BASE_ENDPOINT = "http://localhost:8080"

export default class Category extends Component {
    state = {
        id: null,
        title: "Create a new category",
        name: "",
        requestID: "",
        categories: [],
        isCreate: true,
        error: undefined
    }

    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
        this.onSave = this.onSave.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.clearForm = this.clearForm.bind(this);

    }

    handleChange(event) {
        const target = event.target;
        const name = target.name;
        this.setState({[name]: target.value});
        console.log(this.state);
    }


    async getCategory(id) {
        this.hideError()
        const response = await fetch(BASE_ENDPOINT + "/category/get?id=" + id, {
            method: "GET", mode: "cors", headers: {
                "Authorization": sessionStorage.getItem("jwt"), "Content-Type": "application/json",
            }, redirect: "follow", referrer: "no-referrer",
        });

        const data = await response.json();

        this.setState({name: data.name})
        this.setState({requestID: data.requestedID})
        this.setState({id: data.id})
        this.setState({title: "Category ID:" + data.id})
        this.setState({isCreate: false})
    }

    async searchCategories(categoryNamePart = "") {
        const response = await fetch(BASE_ENDPOINT + "/category/search?name=" + categoryNamePart, {
            method: "GET", mode: "cors", headers: {
                "Authorization": sessionStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
            redirect: "follow",
            referrer: "no-referrer",
        });

        const data = await response.json();
        this.setState({categories: data})
    }

    async onDelete() {
        this.hideError()
        const endpoint = BASE_ENDPOINT + "/category/delete?id=" + this.state.id
        const response = await fetch(endpoint, {
            method: "POST",
            mode: "cors",
            headers: {
                "Authorization": sessionStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
            redirect: "follow", referrer: "no-referrer",
            body: JSON.stringify({
                name: this.state.name,
                requestedID: this.state.requestID
            })
        })

        if (!response.ok) {
            this.setState({error: {message: "Category can't be deleted"}})
            return
        }
        this.deleteCategoryFromSearchResults()
        this.clearForm()

    }

    async onSave() {
        this.hideError()
        if (!this.checkFormInput()) return
        console.log(this.state)
        const endpoint = this.state.isCreate ? BASE_ENDPOINT + "/category/create" : BASE_ENDPOINT + "/category/update?id=" + this.state.id
        console.log(endpoint)
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
                requestedID: this.state.requestID
            })
        })

        if (!response.ok) {
            this.setState({error: {message: "Category already exists"}})
            return
        }

        await this.searchCategories("")
        this.clearForm()
    }


    clearForm() {
        this.hideError()
        this.setState({title: "Create a new category"})
        this.setState({id: null, name: "", requestID: "", isCreate: true});
    }


    hideError() {
        this.setState({["error"]: undefined})
    }

    deleteCategoryFromSearchResults() {
        for (let i = 0; i < this.state.categories.length; i++) {
            if (this.state.categories[i].id === this.state.id) {
                this.state.categories.splice(i, 1)
            }
        }
    }

    checkFormInput() {
        try {
            if (this.state.name === "") throw Error("Invalid name")
            if (this.state.text === "") throw Error("Invalid requestID")
            return true;
        } catch (err) {
            this.setState({error: {message: err.message}})
            return false;
        }
    }

    componentDidMount() {
        console.log(sessionStorage.getItem("jwt"))
    }

    render() {
        return (<div className="container">
                {sessionStorage.getItem("jwt") === null && <Navigate to="/sign_in" replace={true}/>}
                <header className="header">
                    <nav className="header__nav">
                        <a href="/banner" className="header__link">Banners</a>
                        <a href="/category" className="header__link header__link_active">Categories</a>
                    </nav>
                </header>

                <main className="main">
                    <aside className="sidebar">
                        <header className="sidebar__header">Categories:</header>

                        <section className="sidebar__content">
                            <div className="sidebar__search">
                                <input className="sidebar__search-input" type="text"
                                       onChange={(event) => this.searchCategories(event.target.value)}
                                       placeholder="Enter category name..."/>
                                <span className="sidebar__search-icon"></span>
                            </div>
                            <div className="sidebar__menu">
                                {this.state.categories !== undefined && this.state.categories.map(item => {
                                    return (<a href="#"
                                               className="sidebar__menu-item"
                                               onClick={() => this.getCategory(item.id)}> {item.name} </a>);
                                })}
                            </div>
                        </section>

                        <footer className="sidebar__footer">
                            <button className="sidebar__submit-button" onClick={this.clearForm}>Create new Category
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
                                        <input className="content__input"
                                               value={this.state.name}
                                               name="name"
                                               onChange={this.handleChange}
                                               type="text"
                                        />
                                    </div>
                                </div>
                                <div className="content__form-item">
                                    <div className="content__form-item-title">Request ID</div>
                                    <div className="content__form-item-content">
                                        <input className="content__input"
                                               value={this.state.requestID}
                                               name="requestID"
                                               onChange={this.handleChange}
                                               type="text"
                                        />
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

                    </section>
                    {this.state.error !== undefined ?
                        <div className="error">
                            <span className="error__text">{this.state.error.message}</span>
                        </div>
                        : null

                    }
                </main>
            </div>

        )
    }
}