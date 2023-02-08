/** @type {import('next').NextConfig} */
const removeImports = require('next-remove-imports')();

const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  basePath: '/paw-2022b-6',
  // i18n: {
  //   locales: ["en-US","es-ES"],
  //   defaultLocale: "en-US",
  // },
  // esmExternals: true
}

module.exports = removeImports(nextConfig)
