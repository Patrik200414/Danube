import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import { Button, CardActionArea, Divider, Rating } from '@mui/material';
import { convertBase64ToObjectUrlForImage } from '../../../utility/imageUtilities';
import { useState } from 'react';
import { Link } from 'react-router-dom';
import styled from 'styled-components';
import Carousel from '../../Carousel/Carousel';

const StyledLink = styled(Link)({
    color: '#000',
    textDecoration: 'none',
});

const StyledCard = styled(Card)({
    width: '345px',
    height: '640px',
    padding: '10px',
    border: '1px solid #D3D3D3',
    marginBottom: '2%',
});

const StyledDetailContainer = styled('div')({
    marginTop: '10px'
});

const StyledCardMedia = styled(CardMedia)({
    maxWidth: '300px',
    maxHeight: '310px',
    margin: 'auto'
});

const StyledCardActionArea = styled(CardActionArea)({
    maxHeight: '550px'
})


export default function ProductCard({ product }) {
    const [imageNumber, setImageNumber] = useState(0);

    return (
        <StyledCard>
            <StyledCardActionArea>
                <StyledCardMedia
                    component="img"
                    image={convertBase64ToObjectUrlForImage(product.images[imageNumber].imageFile)}
                    alt="green iguana"
                />
                <Carousel
                    images={product.images}
                    currSeledIndex={imageNumber}
                    onCarouselClick={(imageNumber) => setImageNumber(imageNumber)}
                />
                <CardContent>
                    <Typography gutterBottom variant="h6" component="div">
                        {product.productName}
                    </Typography>
                    <Typography variant="p" gutterBottom component="div">
                        US ${product.price}
                    </Typography>
                    <Divider />
                    <StyledDetailContainer>
                        <Typography variant="p" gutterBottom component="div">
                            Shipping price: US ${product.shippingPrice}
                        </Typography>
                        <Typography variant="p" gutterBottom component="div">
                            Sold: {product.sold}
                        </Typography>
                        <Typography variant="p" gutterBottom component="div">
                            Quantity: {product.quantity}
                        </Typography>
                        <Typography variant="p" gutterBottom component="div">
                            Delivery time in day: {product.deliveryTimeInDay}
                        </Typography>
                    </StyledDetailContainer>
                    <Rating name="rating" value={product.rating} readOnly />
                </CardContent>
            </StyledCardActionArea>
            <StyledLink to={`/item/${product.id}`}>
                <Button variant="contained" size="small" color="warning">
                    Visit Product!
                </Button>
            </StyledLink>
        </StyledCard>
    );
}