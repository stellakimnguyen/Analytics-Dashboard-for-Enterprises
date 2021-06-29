import React from 'react';
import { Switch, Route } from 'react-router-dom';

import Reports from './pages/Reports.js';
import BikeProduction from './pages/BikeProduction.js';
import Inventory from './pages/Inventory.js';
import Logistics from './pages/Logistics';
import Manufacturing from './pages/Manufacturing.js';
import Accounting from './pages/Accounting.js';
import CreateUser from './pages/CreateUser.js';
import Login from './pages/Login.js';
import SuperUserRoute from "./SuperUserRoute";
import EmployeeRoute from "./EmployeeRoute";

const Main = () => {
  return (
    <Switch> {/* The Switch decides which component to show based on the current URL.*/}
        <EmployeeRoute exact path='/reports' component={Reports}></EmployeeRoute>
        <EmployeeRoute exact path='/bikeproduction' component={BikeProduction}></EmployeeRoute>
        <EmployeeRoute exact path='/inventory' component={Inventory}></EmployeeRoute>
        <EmployeeRoute exact path='/logistics' component={Logistics}></EmployeeRoute>
        <EmployeeRoute exact path='/manufacturing' component={Manufacturing}></EmployeeRoute>
        <SuperUserRoute exact path='/accounting' component={Accounting}></SuperUserRoute>
        <SuperUserRoute exact path='/createuser' component={CreateUser}></SuperUserRoute>
        <Route exact path='/login' component={Login}></Route>
    </Switch>
  );
}

export default Main;