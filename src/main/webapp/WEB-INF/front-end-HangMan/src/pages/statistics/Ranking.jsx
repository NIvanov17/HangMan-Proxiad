import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Loader from "../../Components/Loader/Loader";
import PaginationComponent from "../../Components/Pagination/PaginationComponent";

const Ranking = () => {

    const [playerData, setPlayerData] = useState([]);
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();
    const [currentPage, setCurrentPage] = useState(0);
    const [itemsPerPage] = useState(5);
    const [totalPages, setTotalPages] = useState(0);

    const handlePageChange = (page) => {
        setCurrentPage(page);
    };

    useEffect(() => {
        setIsPending(true);
        fetch(`http://localhost:8080/api/v1/players/ranking?page=${currentPage}&size=${itemsPerPage}`)
            .then(res => res.json())
            .then(data => {
                setIsPending(false);
                setTotalPages(data.totalPages);
                setPlayerData(data.content);
            });
    }, [currentPage])

    const handleSubmit = () => {
        navigate('/rankings/top-ten');
    }

    return (
        <div className="ranking">
            {
                isPending ? (
                    <div className="loader-container">
                        <Loader />
                    </div>
                ) : (
                    <>
                        <form onSubmit={handleSubmit}>
                            <button>Load Top 10 Players</button>
                        </form>
                        <h2>All Time Ranking</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Player ID</th>
                                    <th>Username</th>
                                    <th>Total Wins</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    playerData.map((player, index) => (
                                        <tr key={index}>
                                            <td>{player.id}</td>
                                            <td>{player.username}</td>
                                            <td>{player.totalWins}</td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                        <PaginationComponent currentPage={currentPage}
                            totalPages={totalPages}
                            onPageChange={(page) => setCurrentPage(page)} />
                    </>

                )}
        </div>
    );

}

export default Ranking;