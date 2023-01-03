/** @type {import('next').NextConfig} */
const removeImports = require('next-remove-imports')();
// module.exports = ;
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  i18n: {
    locales: ["en-US","es-ES"],
    defaultLocale: "en-US",
  },
  esmExternals: true
}

module.exports = removeImports(nextConfig)
