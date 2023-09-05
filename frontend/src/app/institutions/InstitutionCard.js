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
import Button from '@mui/material/Button'
import useWindowSize from '../menu/dish/[id]/Reviews/useWindow'
export default function InstitutionCard({ cafe }) {

    const {user, _} = useGlobalContext()

    const [open, setOpen] = useState(false)

    const { width, height } = useWindowSize()

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
          <div style={{marginRight:height > 1200 ? 0 : 10}}>
            <h1 className={styles.property}>{cafe?.locationEN}</h1>
            <p className={styles.property}>Phone number: {cafe.phoneNumber}</p>
            <Button variant='contained'  fullWidth  sx={{marginBottom:1, bgcolor:"#4caf50", "&:hover":{bgcolor:"#4caf50"}}}>
              <p>
                <Link href="https://www.google.com/maps">View on a map</Link>
              </p>
            </Button>
            <Button variant='contained'  fullWidth    sx={{marginBottom:1, bgcolor:"#4caf50", "&:hover":{bgcolor:"#4caf50"}}}>
              <p>
                <Link href={`institutions/institution/${cafe?.id}`}>View reviews</Link>
              </p>
            </Button>
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