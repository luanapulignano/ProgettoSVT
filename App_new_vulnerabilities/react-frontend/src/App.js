import React from "react";
import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Header from "./components/root/fragments/header/Header";
import Home from "./components/root/home/Home";
import SignUp from "./components/root/users/signUp/SignUp";
import RegisterBusiness from "./components/root/users/signUp/RegisterBusiness";
import Login from "./components/root/users/login/Login";
import UserHome from "./components/root/users/UsersHome";
import Hobbie from "./components/root/users/business/Offer/Hobbie";
import AccountUser from "./components/root/users/user/accountUser/AccountUser";
import AccountBusiness from "./components/root/users/business/AccountBusiness/AccountBusiness";
import TestForm from "./components/root/users/user/test/TestForm";
import CreateOffer from "./components/root/users/business/Offer/CreateOffer";
import MyHobbies from "./components/root/users/user/MyHobbies";
import ProtectedRoutesGuest from "./components/protectedRoutes/ProtectedRoutesGuest";
import ProtectedRoutesUser from "./components/protectedRoutes/ProtectedRoutesUser";
import ProtectedRoutesBusiness from "./components/protectedRoutes/ProtectedRoutesBusiness";
import EditUserProfile from "./components/root/users/user/accountUser/EditUserProfile";
import EditBusinessProfile from "./components/root/users/business/AccountBusiness/EditBusinessProfile";
import UpdateOffer from "./components/root/users/business/Offer/UpdateOffer";
import PasswordChange from "./components/root/users/login/forgottenPassword/PasswordChange";
import SetUpNewPassword from "./components/root/users/login/forgottenPassword/SetUpNewPassword";
import AuthenticationService from "./api/authentication/AuthenticationService";
import { useState } from "react";
import { useEffect } from "react";
function App() {
  const [username, setUsername] = useState("");
  const [error, setError] = useState(null);

  return (
    <Router>
      <div className="App">
        <Header username={username} error={error} setUsername={setUsername} setError={setError}/>
        <Routes>
          <Route element={<ProtectedRoutesGuest />}>
            <Route path="/" element={<Home />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/register-business" element={<RegisterBusiness />} />
            <Route path="/login" element={<Login />} />
            <Route path="/change-password" element={<PasswordChange />} />
            <Route path="/password/:id" element={<SetUpNewPassword />} />
          </Route>
          <Route element={<ProtectedRoutesUser url={window.location.href}/>}>
            <Route path="/edit-profile" element={<EditUserProfile />} />
            <Route path="/user-home" element={<UserHome />} />
            <Route path="/account-user" element={<AccountUser />} />
            <Route path="/test" element={<TestForm />} />
            <Route path="/my-hobbies" element={<MyHobbies />} />
            <Route path="/hobbie/:id" element={<Hobbie />} />
          </Route>

          <Route element={<ProtectedRoutesBusiness />}>
            <Route
              path="/edit-business-profile"
              element={<EditBusinessProfile />}
            />
            <Route path="/edit-offer" element={<UpdateOffer />} />
            <Route path="/business-home" element={<UserHome />} />
            <Route path="/account-business" element={<AccountBusiness />} />
            <Route path="/create-offer" element={<CreateOffer />} />
            <Route path="/account-business" element={<AccountBusiness />} />
            <Route path="/offer/:id" element={<Hobbie />} />
          </Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
