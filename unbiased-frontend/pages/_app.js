import "../styles/http_cdn.rawgit.com_xcatliu_simplemde-theme-dark_master_dist_simplemde-theme-dark.css"

import '../styles/bootstrap.min.css'
import '../styles/custom.css'
// import "@uiw/react-md-editor/markdown-editor.css";
import Script from "next/script"
import Navbar from "../components/Navbar";
import AppWrapper from "../context"


function MyApp({ Component, pageProps }) {
  return <div id="__next" className="d-flex h-100 flex-column">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css"/>
    <Script src="/js/http_code.jquery.com_jquery-3.5.1.slim.js"
            strategy="beforeInteractive"></Script>
    <Script src="/js/http_cdn.jsdelivr.net_npm_bootstrap@4.5.3_dist_js_bootstrap.bundle.js"
            ></Script>
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
