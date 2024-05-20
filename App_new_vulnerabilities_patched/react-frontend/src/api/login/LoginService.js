import axios from "../customAxiosConfig/CustomAxiosConfig";

const LoginService = async (username, backUrl) => {
  try {
    const response = await axios.post(`/login`, null, {
      params: {
        username,
      },
    });

    if (response.status === 200) {
      /*
      if (backUrl) {
        window.location.href = backUrl;
      } else {
        window.location.href = '/user-home';
      }*/
      safeRedirect(backUrl);
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

function safeRedirect(url) {
  const allowedDomain = 'http://localhost:4200/';
  try {
      const targetUrl = new URL(url, allowedDomain);
      if (targetUrl.origin === new URL(allowedDomain).origin && url != null) {
          window.location.href = targetUrl.href;
      } else {
        window.location.href = '/user-home';
      }
  } catch (e) {
      console.error('Invalid URL provided:', url);
  }
}

export default LoginService;