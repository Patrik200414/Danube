import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import verifySellerRole from '../utility/verifySellerRole';
import fetchGetAuthorization from "../utility/fetchGetAuthorization";
import ProductsTable from "../component/product/ProductsTable";

import buttonObjectGenerator from '../utility/buttonObjectGenerator';

function ProductUpdate(){
    const [, setUser] = useState();
    const [error, setError] = useState();
    const [myProducts, setMyProducts] = useState();

    const navigate = useNavigate();

    useEffect(() => {
        const userData = JSON.parse(sessionStorage.getItem('USER_JWT'));

        if(!verifySellerRole(userData)){
            navigate('/')
        }

        setUser(userData);
        
        const getUserProducts = async () => {
            const userProducts = await fetchGetAuthorization(`/api/product/myProducts/${userData.id}`, userData.jwt);
            const productsResponse = await userProducts.json();
            if(userProducts.ok){
                setMyProducts(productsResponse);
            } else {
                setError(productsResponse.errorMessage);
            }
        }

        getUserProducts();

    }, [navigate])

    return(
        <div className="product-update-container">
            {error && <h1 key={error} className="item-not-found-error">{error}</h1>}
            {myProducts &&
                <>
                    <h2>Product update</h2>
                    <ProductsTable products={myProducts} buttons={[
                        buttonObjectGenerator('Update', 'Update', true, '/item'),
                        buttonObjectGenerator('Delete', 'Delete', true, '', (id) => console.log(`Delete ${id}`))
                        ]}/>
                </>
            }
        </div>
    )
}

export default ProductUpdate;