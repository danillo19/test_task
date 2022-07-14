import * as React from "react";
import {Navigate} from "react-router";
import "../styles/sign_in.css";

export default class SignIn extends React.Component {
    state = {
        username: "",
        password: "",
        isAuthorized: false,
        error: false
    };

    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        const target = event.target;
        const name = target.name;
        this.setState({[name]: target.value});
    }

    async handleSubmit() {
        console.log("Отправленное имя:", this.state.username);
        const response = await fetch("http://localhost:8080/login/", {
            method: "POST",
            mode: "cors",
            headers: {"Content-Type": "application/json",},
            redirect: "follow",
            referrer: "no-referrer",
            body: JSON.stringify(this.state),
        });
        console.log(response)
        if (!response.ok && response.status === 403) {
            this.setState({error: true})
            return
        }
        const data = await response.json();
        console.log("DATA")
        sessionStorage.setItem("jwt",data.token)
        console.log(sessionStorage.getItem("jwt"))

        this.setState({username: data.username})
        this.setState({isAuthorized: true})
    }

    render() {
        let {isAuthorized} = this.state;
        return (
            <div className="formStyle">

                {isAuthorized && <Navigate to="/banner" replace={true}/>}

                <h3 className="content__header">Sign In</h3>

                <div className="mb-3">
                    <label className="labelStyle">Username</label>
                    <input
                        className="inputStyle"
                        name="username"
                        type="text"
                        placeholder="Enter username"
                        value={this.state.username}
                        onChange={this.handleChange}
                    />
                </div>

                <div className="mb-3">
                    <label className="labelStyle">Password</label>
                    <input
                        name="password"
                        type="password"
                        className="inputStyle"
                        placeholder="Enter password"
                        value={this.state.password}
                        onChange={this.handleChange}
                    />
                </div>
                <div >
                    <button className="submitStyle" onClick={this.handleSubmit}>
                        Submit
                    </button>
                </div>
                {this.state.error !== false ?
                    <div className="error">
                        <span className="error__text">Invalid username/password</span>
                    </div>
                    : null

                }
            </div>
        );
    }
}
