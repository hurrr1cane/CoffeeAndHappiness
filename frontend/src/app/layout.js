import './globals.scss'
import { Raleway } from 'next/font/google'
import Navbar from './components/Navbar'
import Footer from './components/Footer'
export const metadata = {
  title: 'Coffee and Happiness',
  description: 'Coffe and Happiness website',
}

const raleway = Raleway({subsets: ['latin', 'cyrillic']})

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className={raleway.className}>
        <Navbar/>
        <div>{children}</div>
        <Footer/>
      </body>
    </html>
  )
}
