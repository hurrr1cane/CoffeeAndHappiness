"use client"

import styles from './page.module.scss'
import InstitutionCards from './InstitutionCards'
import { useState } from 'react'
import { Fab } from '@mui/material'
import { Add } from '@mui/icons-material'
import CreateCafeModal from './CreateCafeModal'
import { useGlobalContext } from '../store/store'
export default function Home() {

  const [open, setOpen] = useState(false)
  const {user, setUser, language, setLanguage} = useGlobalContext()

  return (
   <main className={styles.main}>
      {user.role === 'ADMIN' && <CreateCafeModal open={open} setOpen={setOpen}/>}
      <h1>Institutions page</h1>
      <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Nostrum corrupti aspernatur dolorem eligendi cum deleniti commodi rem nihil vitae saepe necessitatibus quos esse, blanditiis laboriosam nesciunt doloribus quasi officiis aliquam.</p>
      {user.role === 'ADMIN' && <Fab onClick={() => {setOpen(true)}} sx={{bgcolor:"#7aaf4c", "&:hover": {bgcolor:"#7aaf4c"}}}><Add/></Fab>}
      <InstitutionCards/>
    </main>

  )
}
