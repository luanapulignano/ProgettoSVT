import axios from "../customAxiosConfig/CustomAxiosConfig";

const RetrieveMeteoInfo = (setMeteoData) => {
    console.log("Entro");
    const urlMeteo = "https://api.openweathermap.org/data/2.5/weather?q=New%20York&appid=e6394169c8a50d3307b6ab89b6564804";


    axios.post('/meteo', { url: urlMeteo })
    .then(response => {
        console.log("Risposta dal backend:", response.data);
        setMeteoData(response.data);
    })
    .catch(error => {
        console.error('Si è verificato un errore nel meteo:', error);
    });
}

export default RetrieveMeteoInfo;
