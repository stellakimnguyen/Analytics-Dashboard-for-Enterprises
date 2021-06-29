import React from "react";
import {Redirect, Route} from "react-router-dom";
import Popup from './components/Popup';

export default function EmployeeRoute({component: Component, ...rest}) {
    const role = localStorage.getItem("role");
    return (
        <Route {...rest} render={(props) => {
            return role === "ROLE_ADMIN" || role === "ROLE_MANAGER" || role === "ROLE_USER"
                ? <Component {...props} />
                : 
                <>
                    <Redirect to='/login' />
                    {alert("You do not have access.")}
                </>
        }} />
    )
}