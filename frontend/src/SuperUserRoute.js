import React from "react";
import {Redirect, Route} from "react-router-dom";

export default function SuperUserRoute({component: Component, ...rest}) {
    const role = localStorage.getItem("role");
    return (
        <Route {...rest} render={(props) => {
            return role === "ROLE_ADMIN" || role === "ROLE_MANAGER"
                ? <Component {...props} />
                : 
                <>
                    <Redirect to='/login' />
                    {alert("You do not have access.")}
                </>
        }} />
    )
}