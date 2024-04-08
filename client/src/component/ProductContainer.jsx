import { useEffect, useState } from "react";
import ProductCard from "./ProductCard";

function ProductContainer(){
    const [products, setProducts] = useState();
    const [itemPerPage, setItemPerPage] = useState(9);
    const [pageNumber, setPageNumber] = useState(0);

    const [error, setError] = useState();

    useEffect(() => {
        const getProducts = async () => {
            const productsData = await fetch(`/api/product?pageNumber=${pageNumber}&itemPerPage=${itemPerPage}`);
            const productResponse = await productsData.json();
            console.log(productResponse);
            if(productsData.ok){
                setProducts(productResponse);
                setError();
            } else{
                setError(productResponse.errorMessage);
            }
        }

        getProducts();
    }, [itemPerPage, pageNumber])

    return(
        <div className="product-container">
            {error && <p className="error-message">{error}</p>}
            {products && products.map(product => <ProductCard key={product.id} product={product}/>)}
        </div>
    )
}

export default ProductContainer;