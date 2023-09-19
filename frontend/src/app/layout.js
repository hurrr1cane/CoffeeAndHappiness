import './globals.scss'
import { Montserrat } from 'next/font/google'
import Navbar from './components/Navbar/Navbar'
import Footer from './components/Footer/Footer'
import { GlobalContextProvider } from './store/store'
export const metadata = {
  title: 'Coffee and Happiness',
  description: 'Coffe and Happiness website',
}

const montSerrat = Montserrat({subsets: ['latin', 'cyrillic']})
 
export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className={montSerrat.className}>
        <GlobalContextProvider>
          <Navbar/>
          <div>{children}</div>
          <Footer/>
        </GlobalContextProvider>
      </body>
    </html>
  )
}
