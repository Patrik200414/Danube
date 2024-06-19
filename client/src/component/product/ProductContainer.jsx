import { useEffect, useState } from "react";
import PaginationButton from "../pagination/PaginationButton";
import ProductCard from "./ProductCard";
import Proptypes from "prop-types";

function ProductContainer({pageProducts, onPaginationNumberChange}){
    const [paginationButtons, setPaginationButtons] = useState();

    useEffect(() => {
        if(pageProducts){
            setPaginationButtons(Array.from({length: pageProducts.pageNumber}, (_, index) => <PaginationButton key={index} pageNumber={index} onPaginationNumberChange={(pageNumber) => onPaginationNumberChange(pageNumber)}/>));
        }
    }, [pageProducts])

    return(
        <div className="product-page-container">
            <div className="product-container">
                {pageProducts ? pageProducts.products.map(product => <ProductCard key={product.id} product={product}/>) : <p className="loading-text">Loading...</p>}
            </div>
            {paginationButtons && <div className="pagination-button-container">{paginationButtons}</div>}
        </div>
    )
}

ProductContainer.propTypes = {
    pageProducts: Proptypes.object
}

export default ProductContainer;