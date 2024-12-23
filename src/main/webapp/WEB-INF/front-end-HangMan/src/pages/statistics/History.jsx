
import { useLocation, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import "./History.css";
import Loader from "../../Components/Loader/Loader";
import PaginationComponent from "../../Components/Pagination/PaginationComponent";




const History = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [isPending, setIsPending] = useState(false);
    const { username } = location.state || {};
    const [historyData, setHistoryData] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [itemsPerPage] = useState(6);
    const [totalPages, setTotalPages] = useState(0);

    const resumeSinglePlayer = (gameId) => {
        sessionStorage.setItem("gameId", gameId);
        navigate('/single-player/games');
    }

    const resumeMultiPlayer = (gameId) => {
        navigate('/multi-player/games', { state: { gameId } });
    }

    useEffect(() => {
        setIsPending(true);
        fetch(`http://localhost:8080/api/v1/games/history?page=${currentPage}&size=${itemsPerPage}`, {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${sessionStorage.getItem('token')}`,
            },
        })
            .then((res) => {
                if (res.status === 401) {
                    navigate('/login', { replace: true });
                    return Promise.reject('User is not authenticated');
                };
                return res.json();
            })
            .then(data => {
                setIsPending(false);
                setHistoryData(data.content);
                setTotalPages(data.totalPages || 1);
            })
    }, [currentPage]);


    return (
        <div className="history">
            {isPending ? (
                <div className="loader-container">
                    <Loader />
                </div>
            ) : (
                <>
                    <h2>History</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Mode</th>
                                <th>Tries Left</th>
                                <th>Category</th>
                                <th>Word State</th>
                                <th>Is Finished</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {historyData.map((game) => (
                                <tr key={game.gameId}>
                                    <td>{game.gameId}</td>
                                    <td>{game.gameMode}</td>
                                    <td>{game.triesLeft}</td>
                                    <td>{game.category}</td>
                                    <td>{game.finished ? game.word : game.wordProgress}</td>
                                    <td>{game.finished ? "Yes" : "No"}</td>
                                    <td>
                                        {game.gameMode === 'Single Player' ? (
                                            <button
                                                onClick={() => resumeSinglePlayer(game.gameId)}
                                                disabled={game.finished}
                                            >
                                                Resume Game
                                            </button>
                                        ) : (
                                            <button
                                                onClick={() => resumeMultiPlayer(game.gameId)}
                                                disabled={game.finished}
                                            >
                                                Resume Game
                                            </button>
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                </>

            )}
            <>
                <PaginationComponent currentPage={currentPage}
                    totalPages={totalPages}
                    onPageChange={(page) => setCurrentPage(page)} />
            </>

        </div>
    );
}

export default History;