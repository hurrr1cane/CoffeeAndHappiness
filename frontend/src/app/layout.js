import './globals.scss'
import { Raleway } from 'next/font/google'
import { Sofia_Sans } from 'next/font/google'
import Navbar from './components/Navbar/Navbar'
import Footer from './components/Footer/Footer'
import { GlobalContextProvider } from './store/store'
export const metadata = {
  title: 'Coffee and Happiness',
  description: 'Coffe and Happiness website',
}

const raleway = Raleway({subsets: ['latin', 'cyrillic']})
const sofiaSans = Sofia_Sans({subsets: ['latin', 'cyrillic']})

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className={sofiaSans.className}>
        <GlobalContextProvider>
          <Navbar/>
          <div>{children}</div>
          <Footer/>
        </GlobalContextProvider>
      </body>
    </html>
  )
}
