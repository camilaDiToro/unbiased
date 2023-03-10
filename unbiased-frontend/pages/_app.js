import "../styles/http_cdn.rawgit.com_xcatliu_simplemde-theme-dark_master_dist_simplemde-theme-dark.css"

import '../styles/bootstrap.min.css'
import '../styles/custom.css'
import '../styles/profile.css'
import Script from "next/script"
import Navbar from "../components/Navbar";
import AppWrapper, {useAppContext} from "../context"
import Head from "next/head";
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import TimeAgo from "javascript-time-ago";
import es from "javascript-time-ago/locale/es";
import en from 'javascript-time-ago/locale/en'
import {getResourcePath, resourcePrefix} from "../constants";
import { SnackbarProvider } from 'notistack';

import { withI18n } from "../i18n";
function MyApp({ Component, pageProps }) {
  const router = useRouter()
  useEffect(() => {
  }, [router.isReady]);

  const [addedLocale, setAddLocale] = useState(false)


  if(!addedLocale) {
    TimeAgo.addLocale(en)
    TimeAgo.addLocale(es)
    setAddLocale(true)
  }


  return <SnackbarProvider maxSnack={3}>
    <div id="main"  className="d-flex flex-column">
      {/*<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css"/>*/}
      <Script strategy="beforeInteractive" id="1" src={getResourcePath('/js/http_code.jquery.com_jquery-3.5.1.slim.js')}
      ></Script>
      <Script strategy="afterInteractive" id="2"  src={getResourcePath('/js/http_cdn.jsdelivr.net_npm_bootstrap@4.5.3_dist_js_bootstrap.bundle.js')}
      ></Script>
      <Head>
        <title>unbiased - Home </title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href={getResourcePath('/img/unbiased-logo-circle.png')} />
      </Head>

      <AppWrapper>
        <Navbar/>
        <div className="container-xxl container-fluid flex-grow-1 d-flex flex-column">
          {/*{router.isReady ?       <Component {...pageProps} />*/}
          {/*:<></>}*/}
          <Component {...pageProps} />
        </div>
      </AppWrapper>
    </div>
  </SnackbarProvider>
}

export default withI18n(MyApp)
