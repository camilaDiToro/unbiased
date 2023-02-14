/** @type {import('next').NextConfig} */
const removeImports = require('next-remove-imports')();
const isProd = process.env.ENV === 'prod'

var rightNow = new Date();
var ts = rightNow.toISOString().slice(0,10).replace(/-/g,"");
const nextConfig = {
  reactStrictMode: true,
  trailingSlash: true,
  swcMinify: true,
  trailingSlash:true,
  // customI18n: {
  //   locales: ["en-US","es-ES"],
  //   defaultLocale: "en-US",
  // },
  // esmExternals: true
  basePath: isProd ? '/paw-2022b-6' : undefined,
  generateEtags: false,


  env : {
    baseURL: isProd ? 'http://pawserver.it.itba.edu.ar/paw-2022b-6' : 'http://localhost:8080/webapp_war_exploded',
    resourcePrefix: isProd ? '/paw-2022b-6' : '/.',
    isProd,
    ts
  }
}

module.exports = removeImports(nextConfig)
