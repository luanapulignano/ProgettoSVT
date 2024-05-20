import React from "react";
import styles from "../../../../../css/Navbar.module.css";
import "@ionic/react/css/core.css";
import Menu from "../navbar/mobileMenu/Menu";
import NavLinks from "../navbar/navLinks/NavLinks";


import AuthenticationService from "../../../../../api/authentication/AuthenticationService";
import axios from "../../../../../api/customAxiosConfig/CustomAxiosConfig";

import { useEffect } from "react";
const Navbar = (props) => {

  useEffect(() => {
    fetchUsername();
  },[props.username]);

  const fetchUsername = () => {
    let username = AuthenticationService.getLoggedInUser();
    axios.get("/hello-user", { params: {username} })
      .then(response => {
        props.setUsername(response.data); 
      })
      .catch(error => {
        props.setError(null);
      });
  };

  const createMarkup = () => {
    return { __html: props.username };
  };
  return (
    <nav className={styles.navbar}>
      <Menu />
      <NavLinks />
      {props.error ? <div>Error: {props.error}</div> : <div dangerouslySetInnerHTML={createMarkup()}></div>}
    </nav>
  );
};

export default Navbar;
