/** @type {import('next').NextConfig} */
const removeImports = require('next-remove-imports')();
const isProd = process.env.ENV === 'prod'
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  // i18n: {
  //   locales: ["en-US","es-ES"],
  //   defaultLocale: "en-US",
  // },
  // esmExternals: true
  basePath: isProd ? '/paw-2022b-6' : undefined
}

module.exports = removeImports(nextConfig)
