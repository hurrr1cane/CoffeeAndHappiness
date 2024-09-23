/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: "https",
        hostname: "**", // Allow all HTTPS images
      },
      {
        protocol: "http",
        hostname: "**", // Allow all HTTP images
      },
    ],
    domains: ['localhost'], // Allow images served from localhost
  }
}

module.exports = nextConfig
