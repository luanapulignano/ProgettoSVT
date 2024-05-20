import React from "react";
import BackgroundCover from "./BackgroundCover";
import Presentation from "./Presentation";
import Cover from "./Cover";
import Footer from "../fragments/footer/Footer";
import style from "../../../css/Footer.module.css";
import { useEffect } from "react";
import axios from "../../../api/customAxiosConfig/CustomAxiosConfig";

const Home = () => {

  useEffect(() => {
    const handleLogout = async () => {
      try {
        await axios.post('/');
        console.log("logout effettuato");
      } catch (error) {
        console.error("Error during logout:", error);
      }
    };

    handleLogout();
  }, []);

  return (
    <>
      <main>
        <Presentation />
        <Cover />
        <BackgroundCover />
      </main>
      <Footer class={style.footer_cover} />
    </>
  );
};

export default Home;
