import axios from "../customAxiosConfig/CustomAxiosConfig";
import AuthenticationService from "../authentication/AuthenticationService";

const UserDataService = () => {
  //let id = AuthenticationService.getLoggedInUserId();
  let username = AuthenticationService.getLoggedInUser();
  try {
    return axios.get(`/client`, {
      params: {
        //id,
        username,
      },
    });
  } catch (err) {
    let error = "";
    if (err.response) {
      error += err.response;
    }
    return error;
  }
};

export default UserDataService;
