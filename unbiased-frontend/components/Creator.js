import Link from "next/link";

export default function Creator(props) {
return <>
    <div className="col mb-4">
        <Link className="link" href="/">

            <div className="card h-100 d-flex flex-row">
                <img src="/img/happy-positivity.svg " alt="..."
                     className="quality-indicator" data-toggle="tooltip"
                     data-placement="top" title="AAAAAA" />
                <div className="d-flex justify-content-between p-2 w-100">
                    <div className="d-flex align-items-center w-auto gap-1">
                        <div className="img-container-article">
                            <img className="rounded-circle object-fit-cover mr-1"
                                 src="/img/profile-image.png" alt=""/>
                        </div>
                        <div className="link-text card-name-text text-ellipsis-1">USER</div>

                    </div>
                </div>


            </div>
        </Link>

    </div>
</>
}