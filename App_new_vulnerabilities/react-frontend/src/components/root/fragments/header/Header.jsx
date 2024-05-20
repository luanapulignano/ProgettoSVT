import React from "react";
import styles from "../../../../css/Header.module.css";
import Navbar from "./navbar/Navbar";
import Logo from "./logo/Logo";

import AuthenticationService from "../../../../api/authentication/AuthenticationService";
import axios from "../../../../api/customAxiosConfig/CustomAxiosConfig"

const Header = (props) => {

  return (
    <header className={styles.header}>
      <Logo />
      <Navbar username={props.username} error={props.error} setUsername={props.setUsername} setError={props.setError}/>
    </header>
  );
};

export default Header;
