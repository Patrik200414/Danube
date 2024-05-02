import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import fetchGetAuthorization from "../utility/fetchGetAuthorization";
import ProductsTable from "../component/product/ProductsTable";

import buttonObjectGenerator from '../utility/buttonObjectGenerator';
import useVerifyUser from "../utility/customHook/useVerifyUser";

function ProductUpdate(){
    const [user] = useVerifyUser("ROLE_SELLER");
    const [error, setError] = useState();
    const [myProducts, setMyProducts] = useState();

    const navigate = useNavigate();

    useEffect(() => {
        
        const getUserProducts = async () => {
            const userProducts = await fetchGetAuthorization(`/api/product/myProducts/${user.id}`, user.jwt);
            const productsResponse = await userProducts.json();
            if(userProducts.ok){
                setMyProducts(productsResponse);
            } else {
                setError(productsResponse.errorMessage);
            }
        }

        if(user){
            getUserProducts();
        }

    }, [navigate, user])

    return(
        <div className="product-dashboard-container">
            {error && <h1 key={error} className="item-not-found-error">{error}</h1>}
            {myProducts &&
                <>
                    <h2>Product update</h2>
                    <ProductsTable products={myProducts} buttons={[
                        buttonObjectGenerator('Update', 'Update', true, '/update'),
                        buttonObjectGenerator('Delete', 'Delete', true, '', (id) => console.log(`Delete ${id}`))
                        ]}/>
                </>
            }
        </div>
    )
}

export default ProductUpdate;