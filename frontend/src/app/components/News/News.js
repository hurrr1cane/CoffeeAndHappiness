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

    const {user, _} = useGlobalContext()

    const [open, setOpen] = useState(false)

    const [createOpen, setCreateOpen] = useState(false)

    useEffect(() => {
        axios.get("http://localhost:8080/api/news")
        .then(res => setNews(res.data))
        .catch(err => console.log(err))
    })


    const handleDeleteClick = (id) => {
        axios.delete(`http://localhost:8080/api/news/${id}`, {
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
    <div className={styles.container}>
              {user.role === 'ADMIN' &&  <CreateNewsModal open={createOpen} setOpen={setCreateOpen}/>}
              {user.role === 'ADMIN' && <Fab onClick={() => {setCreateOpen(true)}} sx={{bgcolor:"#7aaf4c", "&:hover": {bgcolor:"#7aaf4c"}}}><Add/></Fab>} 
    <div className={styles.columnContainer}>
      {news.map((newsItem, index) => (
        <div key={newsItem.id} className={styles.card}>
          {user.role === 'ADMIN' && <EditNewsModal item={newsItem} open={open} setOpen={setOpen} id={newsItem.id} token={user.token} />}
          <div className={styles.link}>
            <Image
              className={styles.image}
              src={"/pizza.jpg"}
              height={200}
              width={200}
              alt='picture of a pizza'
            />
            <div>
              <h1 className={styles.property}>{newsItem.titleEN}</h1>
              <p className={styles.property}>{newsItem.descriptionEN}</p>
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