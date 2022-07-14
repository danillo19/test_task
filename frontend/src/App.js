import React from "react";
import "./App.css";
import {BrowserRouter as Router, Routes, Route, Redirect} from "react-router-dom";
import Banner from "./components/banner/banner";
import SignIn from "./components/sign_in/sign_in";
import {Navigate} from "react-router";
import Category from "./components/category/category";

function App() {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<Navigate replace to="/sign_in"/>}/>
                <Route exact path="/sign_in/" element={<SignIn/>}/>
                <Route exact path="/banner/*" element={<Banner/>}/>
                <Route exact path="/category/*" element={<Category/>}/>
            </Routes>
        </Router>
    );
}

export default App;
