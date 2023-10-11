"use client"

import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { useForm } from 'react-hook-form'
import axios from 'axios';
import { useState } from 'react';
import { IconButton, InputAdornment } from '@mui/material';
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import { NextLink } from 'next/link'
import { useGlobalContext } from '../store/store';
import { useRouter } from 'next/navigation';
import { Alert, AlertTitle } from '@mui/material';

export default function Login() {

    const { push } = useRouter();
    const {isDark, setUser, language} = useGlobalContext()
    const [showPassword, setShowPassword] = useState(false);
    const handleClickShowPassword = () => setShowPassword(!showPassword);
    const { handleSubmit, register, formState: { errors } } = useForm();
    const [error, setError] = useState("")
    const [showAlert, setShowAlert] = useState(false)
    const [showSuccessAlert, setShowSuccessAlert] = useState(false)

    const onSubmit = (data) => {

        setUser({
          email: data.email
        })
        axios.post('https://coffee-and-happiness-backend.azurewebsites.net/api/auth/login', {
            email:data.email,
            password: data.password
        })
        .then(res => {
            setUser(prev => (
              {...prev, token:res.data.accessToken, refreshToken: res.data.refreshToken, date: Date.now()}
            ))
            setShowSuccessAlert(true)
            setTimeout(() => {
              push('/user')
            }, 1000)
            
        })
        .catch(err => {setError(err.response.data.errorMessage); setShowAlert(true)})

        
    }

    return (
        <Container component="main" maxWidth="xs">
          <Alert severity='success' onClose={() => {setShowSuccessAlert(false)}} sx={{display: showSuccessAlert ? "flex" : "none", marginTop:8, marginBottom: 1}}><AlertTitle>{language === 'en' ? 'Logged in successfully' : "Вхід успішний"}
          </AlertTitle> {language === 'en' ? 'You will be redirected shortly' : "Вас незабаром перенаправлять"}</Alert>
          <Alert onClose={() => {setShowAlert(false)}} sx={{display: showAlert ? "flex" : "none"}}
           severity="error">{error}</Alert>
            <Box
              sx={{
                marginTop: 2,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
              }}
            >
              <Avatar sx={{ m: 1, bgcolor:isDark ? "#6b6b6b" : '#4caf50' }}>
                <LockOutlinedIcon />
              </Avatar>
              <Typography component="h1" variant="h5" sx={{color: isDark && "#CCCCCC"}}>
              {language === 'en' ? 'Log in' : "Логін"}
              </Typography>
              <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate sx={{ mt: 1 }}>
                <TextField
                  color="success"
                  {...register('email', {
                    required: language === 'en' ? 'Email address is required' : "Електронна пошта обов'язкова",
                    pattern: {
                      value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i,
                      message: language === 'en' ? 'Invalid email address' : "Неправильна електронна пошта"
                    }
                  })}
                  margin="normal"
                  required
                  fullWidth
                  id="email"
                  label={language === 'en' ? 'Email address' : "Електронна пошта"}
                  name="email"
                  autoComplete="email"
                  autoFocus
                  sx={{
                    input : {
                      color: isDark && "#CCCCCC",
                    }
                  }}
                  error={!!errors.email}
                  helperText={errors.email?.message}
                />
                <TextField
                color="success"
                  {...register('password', {
                    required: language === 'en' ? 'Password is required' : "Пароль обов'язковий",
                  })}
                  margin="normal"
                  required
                  fullWidth
                  name="password"
                  label={language === 'en' ? 'Password' : "Пароль"}
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
                  sx={{ mt: 3, mb: 2, bgcolor:isDark ? "#388E3C" : "#4caf50", '&:hover':{bgcolor:isDark ? "#388E3C" : "#4caf50"} }}
                >
                  {language === 'en' ? 'Log in' : "Увійти"}
                </Button>
                <Grid container>
                  <Grid sx={{color: isDark && "#CCCCCC"}} item xs>
                  <Link component={NextLink} href='/forgotpassword' variant='body2'>
                  {language === 'en' ? 'Forgot password?' : "Забули пароль?"}
                    </Link>
                  </Grid>
                  <Grid sx={{color: isDark && "#CCCCCC"}} item>
                    <Link component={NextLink} href='/register' variant='body2'>
                    {language === 'en' ? "Don't have an account? Register" : "Немає аккануту? Зареєструйся"}
                    </Link>
                  </Grid>
                </Grid>
              </Box>
            </Box>
        </Container>
    )
}
