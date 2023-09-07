import Accordion from '@mui/material/Accordion';
import AccordionDetails from '@mui/material/AccordionDetails';
import AccordionSummary from '@mui/material/AccordionSummary';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import Typography from '@mui/material/Typography';
import { Stack } from '@mui/material';

export default function Order({ expanded, order, id, date, handleChange, width }){





  function convertTimestampToFormattedDate(timestampString) {
    const date = new Date(timestampString);
  
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
  
    const formattedDate = `${day}-${month}-${year}`;
    return formattedDate;
  }

    return (
        <Accordion disableGutters sx={{
                width: width > 768 ? "auto" : width - 30,
                flexGrow: 1,
                fontWeight: 500,
                
                }} onChange={handleChange(id)} expanded={expanded === id}>
            <AccordionSummary
          expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1bh-content"
          id="panel1bh-header"
        >
          <Typography variant='h6' fontWeight="400" sx={{ width: '33%', flexShrink: 0 }}>
          {order.foods.map((food, index) => (
          `${food.nameEN}${index !== order.foods.length - 1 ? ', ' : ''}`
          ))}
          </Typography>
          <Typography sx={{ color: 'text.secondary', marginLeft: width < 425 && "3rem" }}>{convertTimestampToFormattedDate(order.orderDate)}</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Typography component="span">
            <Stack sx={{whiteSpace:"pre-wrap"}} direction="row" alignItems="center">
            {order.foods.map((food, index) => (
               `${food.nameEN}${index !== order.foods.length - 1 ? '\n'  : ''}`
            ))}  
            </Stack>
            
          </Typography>
        </AccordionDetails>
        </Accordion>
    )
}