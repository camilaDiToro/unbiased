import "../styles/http_cdn.rawgit.com_xcatliu_simplemde-theme-dark_master_dist_simplemde-theme-dark.css"

import '../styles/bootstrap.min.css'
import '../styles/custom.css'
import '../styles/profile.css'
// import "@uiw/react-md-editor/markdown-editor.css";
import Script from "next/script"
import Navbar from "../components/Navbar";
import AppWrapper from "../context"


function MyApp({ Component, pageProps }) {
  return <div id="main"  className="d-flex flex-column">
    {/*<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css"/>*/}
    <Script strategy="beforeInteractive" id="1" src="/js/http_code.jquery.com_jquery-3.5.1.slim.js"
            ></Script>
    <Script strategy="afterInteractive" id="2"  src="/js/http_cdn.jsdelivr.net_npm_bootstrap@4.5.3_dist_js_bootstrap.bundle.js"
            ></Script>
    <Script strategy="lazyOnload" id="3" order={3} src="/js/script.js"></Script>


    <AppWrapper>
      <Navbar/>
      <div className="container-xxl container-fluid flex-grow-1">
      <Component {...pageProps} />
      </div>
    </AppWrapper>
  </div>
}

export default MyApp
