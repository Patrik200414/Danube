import PropTypes from 'prop-types';
import ProductsTable from "../component/product/ProductsTable";
import buttonObjectGenerator from "../utility/buttonObjectGenerator";
import useGetCartItems from "../utility/customHook/useGetCartItems";
import fetchDeleteAuthorization from "../utility/fetchDeleteAuthorization";
import getNavbarInformation from "../utility/getNavbarInformation";

function MyCart({onNavbarInformationChange}){
    const [cartItems, setCartItems, error] = useGetCartItems();

    function handleDeleteion(orderId){
        const currUser = JSON.parse(sessionStorage.getItem('USER_JWT'));
        if(currUser){
            handleRegisteredDeletion(orderId, currUser);
        } else{
            handleUnregisteredDeletion(orderId);
        } 
    }

    async function handleRegisteredDeletion(orderId, currUser){
        const removeItemResponse = await fetchDeleteAuthorization(`/api/cart/${orderId}`, currUser.jwt);

        if(removeItemResponse.ok){
            setCartItems(prev => prev.filter(order => order.id !== orderId));
            onNavbarInformationChange(getNavbarInformation(currUser));
        }
    }

    function handleUnregisteredDeletion(orderId){
        const updatedCartItems = cartItems.filter(item => item.id !== orderId);
        setCartItems(updatedCartItems);
        localStorage.setItem('CART_ITEMS', JSON.stringify(updatedCartItems));
        onNavbarInformationChange(getNavbarInformation());
    }

    return (
        <div className="my-cart-dashboard">
            {error && <h1 className="error-message">{error}</h1>}
            {cartItems?.length ? <ProductsTable products={cartItems} buttons={[
                buttonObjectGenerator('removeItem', 'Remove item', true, '', (id) => handleDeleteion(id))
            ]} keyName={'orderId'}/> 
            : 
            cartItems !== undefined ? <h1 className="empty-cart-title">Your Danube Cart is empty!</h1> : ''}
            
        </div>
    )
}

MyCart.propTypes = {
    onNavbarInformationChange: PropTypes.func
}

export default MyCart;