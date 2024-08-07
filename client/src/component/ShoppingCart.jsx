
import PropTypes from 'prop-types'
import { Link } from "react-router-dom";
import styled from 'styled-components';

const StyledCartLink = styled(Link)({
    color: 'white',
    textDecoration: 'none',
    margin: 'auto'
})

function ShoppingCart({itemCount}){

    return(
        <StyledCartLink to='/cart'>
            <div className="shopping-cart">
                <i className="fa fa-shopping-cart"></i>
                <p>{itemCount}</p>
            </div>
        </StyledCartLink>
    )
}

ShoppingCart.propTypes = {
    itemCount: PropTypes.number
}

export default ShoppingCart;