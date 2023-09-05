"use client"

import styles from './InstitutionCard.module.scss'
import Image from 'next/image'
import Link from 'next/link'
import { useGlobalContext } from '../store/store'
import { useState } from 'react'
import {Fab, Stack} from "@mui/material"
import { Edit } from '@mui/icons-material'
import { Close } from '@mui/icons-material'
import EditCafeModal from './EditCafeModal'
export default function InstitutionCard({ cafe }) {

  const {user, setUser, language, setLanguage} = useGlobalContext()

    const [open, setOpen] = useState(false)

    const handleDeleteClick = () => {
        axios.delete(`http://localhost:8080/api//${cafe.id}`, {
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
        <div className={styles.card}>
        {user.role === 'ADMIN' && (
          <EditModal
            item={cafe}
            open={open}
            setOpen={setOpen}
            id={cafe.id}
            token={user.token}
          />
        )}
      
          <Image
            src={cafe?.imageUrl}
            className={styles.image}
            alt={'image of a restaurant'}
            height={200}
            width={200}
          />
          <div>
            <h1 className={styles.property}>{cafe?.locationEN}</h1>
            <p className={styles.property}>Phone number: {cafe.phoneNumber}</p>
            <p className={styles.property}>
              <Link href="https://www.google.com/maps">View on a map</Link>
            </p>
            <p className={styles.property}>
              <Link href={`institutions/institution/${cafe?.id}`}>View reviews</Link>
            </p>
          </div>
        {user.role === 'ADMIN' && (
          <div className={styles.adminActions}>
            <Fab onClick={handleDeleteClick} className={styles.fab}>
              <Close />
            </Fab>
            <Fab onClick={handleEditClick} className={styles.fab}>
              <Edit />
            </Fab>
          </div>
        )}
      </div>      
    )
}