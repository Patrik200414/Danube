function PaginationButton({pageNumber, onPaginationNumberChange}){
    return(
        <button onClick={() => onPaginationNumberChange(pageNumber)} className="pagination-button">{pageNumber + 1}</button>
    )
}

export default PaginationButton;