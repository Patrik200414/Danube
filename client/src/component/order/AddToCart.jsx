import PropTypes from "prop-types";
import { useContext, useState } from "react";
import fetchPostAuthorizationFetch from '../../utility/fetchPostAuthorizationFetch';
import { NavbarContext } from "../../NavbarContext";

function AddToCart({maxQuantity, productId, onNavbarInformationChange, onError, product}){
    const [selectedQuantity, setSelectedQuantity] = useState(1);
    const [user, ] = useState(JSON.parse(sessionStorage.getItem('USER_JWT')));
    const navbarInformation = useContext(NavbarContext)

    function incrementCartNumber(navbarInformation, quantity){
        const incrementedItemCount = navbarInformation.cartItemNumber + Number(quantity);
        navbarInformation.cartItemNumber = incrementedItemCount;
        onNavbarInformationChange(navbarInformation);
    }

    async function addItemToOrdersInRegisteredUser(order){
        order.customerId = user.id;

        const cartData = await fetchPostAuthorizationFetch('/api/cart', user.jwt, JSON.stringify(order), true);
        if(cartData.ok){
            const navbarInfoCopy = {...navbarInformation};
            incrementCartNumber(navbarInfoCopy, order.quantity);
        } else{
            const errorMessage = await cartData.json();
            onError(errorMessage.errorMessage);
        }
    }

    function createCartItemFromProduct(product, order){
        return {
            id: productId,
            image: product.images[0],
            orderedQuantity: order.quantity,
            price: product.productInformation.price,
            productName: product.productInformation.productName,
            rating: product.productInformation.rating
        }
    }

    async function addItemToOrdersInUnregisteredUser(product, order){
        const locallyStoredItems = JSON.parse(localStorage.getItem('CART_ITEMS'));
        const productCartItem = createCartItemFromProduct(product, order);
            if(!locallyStoredItems){
                localStorage.setItem('CART_ITEMS', JSON.stringify([productCartItem]));
            } else{
                const updatedCart = locallyStoredItems.filter(item => item.id !== productCartItem.id);
                updatedCart.push(productCartItem);
                localStorage.setItem('CART_ITEMS', JSON.stringify(updatedCart));
            }

            incrementCartNumber({...navbarInformation}, order.quantity); 
    }

    async function handleAddToCart(e){
        e.preventDefault();
        const order = {
            productId: productId,
            quantity: Number(selectedQuantity),
            customerId: undefined
        }
        if(user){
            addItemToOrdersInRegisteredUser(order)
        } else{
            addItemToOrdersInUnregisteredUser(product, order);
        }
    }

    return(
        <div className="order-quantity-add-to-cart-container">
            <form onSubmit={handleAddToCart}>
                <div>
                    <label htmlFor="itemQuantity">Quantity: </label>
                    <input type="number" id="itemQuantity" min="1" max={maxQuantity} value={selectedQuantity} onChange={e => {
                        const quantity = e.target.value;
                        if(quantity >= 0 && quantity <= maxQuantity){
                            setSelectedQuantity(quantity);
                        }
                    }}/>
                </div>
                <button className="submit">Add to cart</button>
            </form>
            
        </div>
    )
}

AddToCart.propTypes = {
    maxQuantity: PropTypes.number,
    productId: PropTypes.number,
    onNavbarInformationChange: PropTypes.func,
    onError: PropTypes.func,
    product: PropTypes.object
}

export default AddToCart;