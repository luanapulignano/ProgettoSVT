import React from "react";
import { Outlet } from "react-router-dom";
import AuthenticationService from "../../api/authentication/AuthenticationService";
import { Navigate } from "react-router-dom";
import { useEffect ,useState} from "react";

const useAuth = () => {
  return AuthenticationService.isUserLoggedIn();
};
const ProtectedRoutesUser = (props) => {
  const isAuth = useAuth();
  return isAuth ? <Outlet /> : <Navigate to={`/login?back=${props.url}`} />;
};

export default ProtectedRoutesUser;
