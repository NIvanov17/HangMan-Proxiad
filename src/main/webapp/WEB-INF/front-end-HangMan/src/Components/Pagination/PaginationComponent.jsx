import './Pagination.css';


const PaginationComponent = ({ currentPage, totalPages, onPageChange }) => {
    const generatePageNumbers = () => {
        const pages = [];
        const maxVisiblePages = 5;

        if (totalPages <= maxVisiblePages) {
            for (let i = 0; i < totalPages; i++) {
                pages.push(i);
            }
        } else {
            pages.push(0);

            if (currentPage > 2) {
                pages.push('...');
            }

            const start = Math.max(1, currentPage - 1);
            const end = Math.min(totalPages - 2, currentPage + 1);

            for (let i = start; i <= end; i++) {
                pages.push(i);
            }

            if (currentPage < totalPages - 3) {
                pages.push("...");
            }

            pages.push(totalPages - 1);
        }
        return pages;
    };

    const pages = generatePageNumbers();

    return (
        <div className="pagination">
            <button onClick={() => onPageChange(0)} disabled={currentPage === 0}>
                First
            </button>
            <button onClick={() => onPageChange(currentPage - 1)} disabled={currentPage === 0}>
                Prev
            </button>
            {pages.map((page, index) => page === "..." ? (
                <span key={index} className="ellipsis">
                    ...
                </span>
            ) : (
                <button
                    key={page}
                    onClick={() => onPageChange(page)}
                    disabled={currentPage === page}
                >
                    {page + 1}
                </button>
            ))}
            <button onClick={() => onPageChange(currentPage + 1)} disabled={currentPage === totalPages - 1}>
                Next
            </button>
            <button onClick={() => onPageChange(totalPages - 1)} disabled={currentPage === totalPages - 1}>
                Last
            </button>
        </div>
    );
};

export default PaginationComponent;