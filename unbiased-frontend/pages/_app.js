import '../styles/bootstrap.min.css'
import '../styles/custom.css'
import Script from "next/script"
import Navbar from "../components/Navbar";
import AppWrapper from "../context"


function MyApp({ Component, pageProps }) {
  return <div id="__next" className="d-flex h-100 flex-column">
    <Script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
            integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
            crossOrigin="anonymous"></Script>
    <Script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx"
            crossOrigin="anonymous"></Script>
    <Script defer src="/js/script.js"></Script>
    <AppWrapper>
      <Navbar/>
      <div className="container-xxl container-fluid flex-grow-1">
      <Component {...pageProps} />
      </div>
    </AppWrapper>
  </div>
}

export default MyApp
