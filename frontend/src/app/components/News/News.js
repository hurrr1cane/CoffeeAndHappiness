"use client"

import styles from './news.module.scss'
import axios from 'axios';
import { useEffect, useState } from 'react';
import Image from 'next/image';
import { Close } from '@mui/icons-material';
import { Edit } from '@mui/icons-material';
import {Stack, Fab} from '@mui/material'
import { useGlobalContext } from '@/app/store/store';
import Link from 'next/link';
import EditNewsModal from './EditNewsModal';
import CreateNewsModal from './CreateNewsModal';
import { Add } from '@mui/icons-material';
function News() {

    const [news, setNews] = useState([])

    const {user, isDark, language} = useGlobalContext()

    const [open, setOpen] = useState(false)

    const [createOpen, setCreateOpen] = useState(false)

    useEffect(() => {
        axios.get("https://coffee-and-happiness-backend.azurewebsites.net/api/news")
        .then(res => setNews(res.data))
        .catch(err => console.log(err))
    })


    const handleDeleteClick = (id) => {
        axios.delete(`https://coffee-and-happiness-backend.azurewebsites.net/api/news/${id}`, {
            headers: {
                Authorization: "Bearer " + user.token
            }
        })
        .then(res => console.log(res))
        .catch(err => console.log(err))
    }
 

    const handleEditClick = () => {
        setOpen(true)
    }


  return (
    <div className={`${styles.container} ${isDark ? styles.dark : ""}`}>
              {user.role === 'ADMIN' &&  <CreateNewsModal open={createOpen} setOpen={setCreateOpen}/>}
              {user.role === 'ADMIN' && <Fab onClick={() => {setCreateOpen(true)}} sx={{bgcolor:"#7aaf4c", "&:hover": {bgcolor:"#7aaf4c"}}}><Add/></Fab>} 
    <div className={styles.columnContainer}>
      {news.map((newsItem, index) => (
        <div key={newsItem.id} className={styles.card}>
          {user.role === 'ADMIN' && <EditNewsModal item={newsItem} open={open} setOpen={setOpen} id={newsItem.id} token={user.token} />}
          <div className={styles.link}>
            <Image
              className={styles.image}
              src={newsItem.imageUrl || '/placeholder.png'}
              height={200}
              width={200}
              alt='picture of a cafe'
            />
            <div>
              <h1 className={styles.property}>{language === 'en' ?  newsItem.titleEN : newsItem.titleUA}</h1>
              <p className={styles.property}>{language === 'en' ? newsItem.descriptionEN : newsItem.descriptionUA}</p>
            </div>
          </div>
          {user.role === 'ADMIN' ? 
            <Stack direction="column">
              <Fab onClick={() => handleDeleteClick(newsItem.id)} sx={{bgcolor:"#FF0000", "&:hover":{bgcolor:"#FF0000"}, marginLeft:"auto", color:"white", marginRight:"1rem", marginBottom:"1rem"}}><Close/></Fab>
              <Fab onClick={handleEditClick} sx={{bgcolor:"#4caf81", "&:hover":{bgcolor:"#4caf81"}, marginLeft:"auto", color:"white", marginRight:"1rem", marginBottom:"1rem"}}><Edit/></Fab>
            </Stack> : ''}
        </div>
      ))}
    </div>
  </div>
);
}


export default News;