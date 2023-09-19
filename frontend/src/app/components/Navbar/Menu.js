"use client"
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import { Fab } from '@mui/material';
import { useState } from 'react';
import LanguageIcon from '@mui/icons-material/Language'
import { useGlobalContext } from '@/app/store/store';
export default function PositionedMenu({ isDark }) {
  const { setLanguage } = useGlobalContext()
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleEnglish = () => {
    setLanguage('en')
    setAnchorEl(null);
  }

  const handleUkrainian = () => {
    setLanguage('ua')
    setAnchorEl(null);
  }

  return (
    <div>
      <Fab sx={{bgcolor:"inherit", "&:hover":{bgcolor:"inherit"}, boxShadow:"none"}} aria-haspopup="true" aria-controls={open ? 'demo-positioned-menu' : undefined} aria-expanded={open ? 'true' : undefined} onClick={handleClick}>
          <LanguageIcon sx={{color: isDark ? "#CCCCCC" : "black"}}/>
      </Fab>
      <Menu
        id="demo-positioned-menu"
        aria-labelledby="demo-positioned-button"
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        anchorOrigin={{
          vertical: 'top',
          horizontal: 'left',
        }}
        transformOrigin={{
          vertical: 'top',
          horizontal: 'left',
        }}
      >
        <MenuItem onClick={handleEnglish}>English 🇬🇧</MenuItem>
        <MenuItem onClick={handleUkrainian}>Українська 🇺🇦</MenuItem>
      </Menu>
    </div>
  );
}