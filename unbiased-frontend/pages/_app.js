import '../styles/bootstrap.min.css'
import '../styles/custom.css'
import Navbar from "../components/Navbar";
import AppWrapper from "../context"

function MyApp({ Component, pageProps }) {
  return <body id="__next" className="d-flex h-100 flex-column">
    <AppWrapper>
      <Navbar/>
      <div className="container-xxl container-fluid flex-grow-1">
      <Component {...pageProps} />
      </div>
    </AppWrapper>
  </body>
}

export default MyApp
