"use client"

import styles from './page.module.scss'
import MenuBar from './MenuBar/MenuBar'
import Dishes from './DishCard/Dishes'
import CreateDishModal from './DishCard/CreateDishModal'
import { Fab } from '@mui/material'
import AddIcon from '@mui/icons-material/Add'
import { useState } from 'react'
import { useGlobalContext } from '../store/store'
export default function Home() {

  const [tab, setTab] = useState("MAIN")
  const {user, isDark} = useGlobalContext()
  const [open, setOpen] = useState(false)

  return (
   <main className={`${styles.main} ${isDark ? styles.dark : ""}`}>
      {user.role === 'ADMIN' &&   <CreateDishModal open={open} setOpen={setOpen}/>}
      <MenuBar tab={tab} setTab={setTab}/>
      <h1>Menu page</h1>
      <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Obcaecati commodi corporis soluta nisi aliquam pariatur hic, illum voluptate omnis mollitia, quas, natus at! Similique dolores accusantium laborum minima repellat! Officiis!</p>
      {user.role === 'ADMIN' && <Fab onClick={() => {setOpen(true)}} sx={{bgcolor:"#7aaf4c", "&:hover": {bgcolor:"#7aaf4c"}}}><AddIcon/></Fab>}
      <Dishes tab={tab}/>
   </main>

  )
}