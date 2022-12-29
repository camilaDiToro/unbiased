import '../styles/globals.css'
import '../styles/bootstrap.min.css'
import '../styles/custom.css'
import Navbar from "../components/Navbar";

function MyApp({ Component, pageProps }) {
  return <>
    <Navbar/>
    <div >
      <Component {...pageProps} />
    </div>
  </>
}

export default MyApp
