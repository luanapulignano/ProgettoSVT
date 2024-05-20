import axios from "../customAxiosConfig/CustomAxiosConfig";

const LoginService = async (username, backUrl) => {
  try {
    const response = await axios.post(`/login`, null, {
      params: {
        username,
      },
    });

    console.log(backUrl);
    if (response.status === 200) {
      if (backUrl) {
        window.location.href = backUrl;
      } else {
        window.location.href = '/user-home';
      }
    }

    return response;
  } catch (err) {
    let error = "";
    if (err.response) {
      error += err.response;
    }
    return error;
  }
};

export default LoginService;