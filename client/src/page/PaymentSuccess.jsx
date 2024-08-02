import { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { fetchPatchAuthorizationFetch } from "../utility/fetchUtilities";

function PaymentSuccess({onNavbarInformationChange}){
    const [orderSuccess, setOrderSucsess] = useState();
    useEffect(() => {
        const orderItem = async () => {
            const DEFAULT_CART_ITEM_AMOUNT = 0;

            const paymentSessionId = JSON.parse(sessionStorage.getItem('ORDER_SESSION'));
            const currUser = JSON.parse(sessionStorage.getItem("USER_JWT"));
            if(currUser && currUser.id == paymentSessionId.userId){
                const orderItemData = await fetchPatchAuthorizationFetch("/api/order/confirm", currUser.jwt, paymentSessionId);
                if(orderItemData.ok){
                    onNavbarInformationChange({
                        userFirstName: currUser.firstName,
                        cartItemNumber: DEFAULT_CART_ITEM_AMOUNT
                    });
                    setOrderSucsess(true);
                } else{
                    setOrderSucsess(false);
                }
            } else{
                setOrderSucsess(false);
            }
        }

        orderItem();
        
    }, [])
    return(
        <div className="success-page-container">
            {orderSuccess !== undefined ? 
                orderSuccess === true ? 
                    <>
                        <h1>Thank you for your purchase!</h1>
                        <p>We successfully recorderd your order!</p>
                    </>

                :
                    <h1>Oh no! Something went wrong! We could not record your order! Please try it again!</h1>
                :
                <p className="loading-text">Loading...</p>
            }
        </div>
    )
}

PaymentSuccess.propTypes = {
    onNavbarInformationChange: PropTypes.func
}

export default PaymentSuccess;