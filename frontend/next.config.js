/** @type {import('next').NextConfig} */
const nextConfig = {
        images: {
          remotePatterns: [
            {
              protocol: 'https',
              hostname: 'coffee-and-happiness-images.s3.eu-central-1.amazonaws.com',
              port: '',
              pathname: '/**',
            },
          ],
        }
}

module.exports = nextConfig
