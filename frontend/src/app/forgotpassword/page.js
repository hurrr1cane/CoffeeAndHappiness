"use client"

import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import EmailOutlinedIcon from '@mui/icons-material/EmailOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { useForm } from 'react-hook-form'
import axios from 'axios';
import { useState } from 'react';
import { NextLink } from 'next/link'
import { useRouter } from 'next/navigation';
import { Alert, AlertTitle } from '@mui/material';
import { IconButton, InputAdornment } from '@mui/material';
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import styles from './page.module.scss'
import { MuiOtpInput } from 'mui-one-time-password-input'
import KeyOutlinedIcon from '@mui/icons-material/KeyOutlined';
import PasswordOutlinedIcon from '@mui/icons-material/PasswordOutlined';
import useWindowSize from '../menu/dish/[id]/Reviews/useWindow';
import { useGlobalContext } from '../store/store';

export default function Home() {

    const { push } = useRouter();
    const { handleSubmit, register, formState: { errors } } = useForm();

    const { isDark } = useGlobalContext()

    const { width, height } = useWindowSize()

    const [showPassword, setShowPassword] = useState(false);
    const handleClickShowPassword = () => setShowPassword(!showPassword);

    const [error, setError] = useState("")
    const [showErrorAlert, setShowErrorAlert] = useState(false)
    const [showSuccessAlert, setShowSuccessAlert] = useState(false)
    const [showOtpAlert, setShowOtpAlert] = useState(false)
    const [showPasswordAlert, setShowPasswordAlert] = useState(false)

    const [showEmailInput, setShowEmailInput] = useState(true)
    const [showOtpInput, setShowOtpInput] = useState(false)
    const [showPasswordInput, setShowPasswordInput] = useState(false)
    const [showButton, setShowButton] = useState(false)
    

    const [email, setEmail] = useState('')

    const [otp, setOtp] = useState('')

    const handleChange = (newValue) => {
        setOtp(newValue)
    }


    function matchIsNumeric(text) {
        const isNumber = typeof text === 'number'
        const isString = typeof text === 'string'
        return (isNumber || (isString && text !== '')) && !isNaN(Number(text))
      }
      
      const validateChar = (value, index) => {
        return matchIsNumeric(value)
      }


    const onEmailSubmit = (data) => {
        axios.post(`https://coffee-and-happiness-backend.azurewebsites.net/api/auth/forgot-password?email=${data?.email}`)
        .then(res => {
            console.log(res)
            setShowSuccessAlert(true)
            setTimeout(() => {setShowEmailInput(false)
            setShowOtpInput(true); setShowSuccessAlert(false)}, 1000)
            setShowErrorAlert(false)
        })
        .catch(err => {
            console.log(err)
            setShowErrorAlert(true)
            setError(err.response.data.message)
        })
        setEmail(data.email)
    }

    const handleOtpSubmit = (value) => {    
        axios.post(`https://coffee-and-happiness-backend.azurewebsites.net/api/auth/validate-verification-code`, {
            verificationCode: otp,
            email: email
        })
        .then(() => {
            setShowOtpAlert(true)
            setShowErrorAlert(false)
            setTimeout(() => {
                setShowOtpInput(false)
                setShowOtpAlert(false)
                setShowPasswordInput(true)
                setShowButton(false)
            }, 1000)
        })
        .catch((err => {
            console.log(err.response.data.message)
            setShowErrorAlert(true)
            setError(err.response.data.message)
        }))       
    }

    const onPasswordSubmit = (data) => {
        console.log(data.password)
        axios.post('https://coffee-and-happiness-backend.azurewebsites.net/api/auth/reset-password', {
            newPassword: data.password,
            email: email
        })
        .then(() => {
            showPasswordAlert(true)
            setTimeout(() => {
                push('/login')
            }, 1000)
        })
        .catch(err => {
            console.log(err)
            setError(err.response.data.message)
            setShowErrorAlert(true)
        })
    }

    return (
        <div className={`${styles.main} ${isDark ? styles.dark : ""}`}>
            <Container component="main" maxWidth="xs">
              <Alert severity='success' onClose={() => {setShowSuccessAlert(false)}} sx={{display: showSuccessAlert ? "flex" : "none", marginTop:8, marginBottom: 1, whiteSpace:"nowrap"}}>Password reset code sent.</Alert>
              <Alert severity='success' onClose={() => {setShowOtpAlert(false)}} sx={{display: showOtpAlert ? "flex" : "none", marginTop:8, marginBottom: 1, whiteSpace:"nowrap"}}>Code validated successfully.</Alert>  
              <Alert severity='success' onClose={() => {setShowPasswordAlert(false)}} sx={{display: showPasswordAlert ? "flex" : "none", marginTop:8, marginBottom: 1, whiteSpace:"nowrap"}}>Password reset, you will be redirected shortly.</Alert>  
              <Alert onClose={() => {setShowErrorAlert(false)}} sx={{display: showErrorAlert ? "flex" : "none"}}
               severity="error">{error}</Alert>
                <Box
                  sx={{
                    marginTop: 2,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                  }}
                >

                {showOtpInput ? 
                (<>
                    <Avatar sx={{ m: 1, bgcolor:isDark ? "#6b6b6b" : '#4caf50'}}>
                        <KeyOutlinedIcon />
                        </Avatar>
                        <Typography sx={{p:1, color: isDark && "#CCCCCC", whiteSpace:width > 500 ? "nowrap" : "default"}} component="h1" variant="h5">
                        Input the code sent to {email}
                        </Typography>
                        <MuiOtpInput sx={{color: isDark && "#CCCCCC", input: {
                            color: isDark && "#CCCCCC"
                        }}} validateChar={validateChar} gap={1} length={6} onComplete={() => setShowButton(true)} value={otp} onChange={handleChange} />
                        <Button
                            onClick={handleOtpSubmit}
                            fullWidth
                            variant="contained"
                            sx={{whiteSpace:"nowrap", mt: 3, mb: 2, bgcolor:isDark ? "#388E3C" : "#4caf50", '&:hover':{bgcolor:isDark ? "#388E3C" : "#4caf50"}, display: showButton ? "block" : "none" }}
                        >
                            Submit password reset code
                        </Button>
                </>) : null
                     
                }


                {showPasswordInput ?
                    (   <>
                        <Avatar sx={{ m: 1, bgcolor:isDark ? "#6b6b6b" : '#4caf50' }}>
                        <PasswordOutlinedIcon />
                        </Avatar>
                        <Typography sx={{whiteSpace:"nowrap", p:1, color: isDark && "#CCCCCC"}} component="h1" variant="h5">
                        Input new password
                        </Typography>
                        <Box component="form" onSubmit={handleSubmit(onPasswordSubmit)} noValidate sx={{ mt: 1 }}>
                        <TextField
                        color="success"
                        {...register('password', {
                            required: 'Password is required',
                        })}
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Password"
                        type={showPassword ? "text" : "password"}
                        sx = {{
                            input : {
                              color: isDark && "#CCCCCC"
                            },
                            fieldset: {
                                outlineColor: "red"
                            }
                          }}
                        InputProps={{ 
                            endAdornment: (
                            <InputAdornment position="end">
                                <IconButton
                                aria-label="toggle password visibility"
                                onClick={handleClickShowPassword}
                    
                                >
                                {showPassword ? <Visibility sx={{color: isDark && "#CCCCCC"}}/> : <VisibilityOff sx={{color: isDark && "#CCCCCC"}}/>}
                                </IconButton>
                            </InputAdornment>)}}
                        id="password"
                        autoComplete="current-password"
                        error={!!errors.password}
                        helperText={errors.password?.message}
                        />
                         <Button
                            type="submit"                  
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, bgcolor:isDark ? "#388E3C" : "#4caf50", '&:hover':{bgcolor:isDark ? "#388E3C" : "#4caf50"}}}
                        >
                            Submit password reset code
                        </Button>
                        </Box>
                        </>
                    )
                    : null
                }

                {showEmailInput ? (
                    <>
                        <Avatar sx={{ m: 1, bgcolor:isDark ? "#6b6b6b" : '#4caf50' }}>
                        <EmailOutlinedIcon />
                        </Avatar>
                        <Typography sx={{whiteSpace:"nowrap", color: isDark && "#CCCCCC"}} component="h1" variant="h5">
                        Reset password
                        </Typography>
                        <Box component="form" onSubmit={handleSubmit(onPasswordSubmit)} noValidate sx={{ mt: 1 }}>
                        <TextField
                           color="success"
                           {...register('password', {
                             required: 'Password is required',
                           })}
                           margin="normal"
                           required
                           fullWidth
                           name="password"
                           label="Password"
                           type={showPassword ? "text" : "password"}
                           sx = {{
                             input : {
                               color: isDark && "#CCCCCC"
                             },
                             fieldset: {
                                 outlineColor: "red"
                             }
                           }}
                           InputProps={{ 
                             endAdornment: (
                               <InputAdornment position="end">
                                 <IconButton
                                   aria-label="toggle password visibility"
                                   onClick={handleClickShowPassword}
                     
                                 >
                                   {showPassword ? <Visibility sx={{color: isDark && "#CCCCCC"}} /> : <VisibilityOff sx={{color: isDark && "#CCCCCC"}}/>}
                                 </IconButton>
                               </InputAdornment>)}}
                           id="password"
                           autoComplete="current-password"
                           error={!!errors.password}
                           helperText={errors.password?.message}
                        />
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, bgcolor:isDark ? "#388E3C" : "#4caf50", '&:hover':{bgcolor:isDark ? "#388E3C" : "#4caf50"}, whiteSpace:"nowrap" }}
                        >
                            Send reset password code
                        </Button>
                        <Grid container>
                            <Grid item xs>
                            </Grid>
                            <Grid  sx ={{color: isDark && "#CCCCCC"}} item>
                            <Link component={NextLink} href='/login' variant='body2'>
                                Already have an account? Log in
                            </Link>
                            </Grid>
                        </Grid>
                        </Box>
                    </>
                    ) : null}
                </Box>
            </Container>
        </div>
    )
}