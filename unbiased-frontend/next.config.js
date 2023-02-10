/** @type {import('next').NextConfig} */
const removeImports = require('next-remove-imports')();
const isProd = process.env.ENV === 'prod'
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  // customI18n: {
  //   locales: ["en-US","es-ES"],
  //   defaultLocale: "en-US",
  // },
  // esmExternals: true
  basePath: isProd ? '/paw-2022b-6' : undefined,

  env : {
    baseURL: isProd ? 'http://localhost/paw-2022b-6' : 'http://localhost:8080/webapp_war_exploded',
    resourcePrefix: isProd ? '/paw-2022b-6' : '/.'
  }
}

module.exports = removeImports(nextConfig)
