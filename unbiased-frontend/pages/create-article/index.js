import Link from "next/link";
import {useState} from "react";


import dynamic from "next/dynamic";
import {useAppContext} from "../../context";
import Tooltip from "../../components/Tooltip";
import ModalTrigger from "../../components/ModalTrigger";
import BackButton from "../../components/BackButton";
import Modal from "../../components/Modal";
import Head from "next/head";
import {useRouter} from "next/router";


const MarkdownEditor = dynamic(
    () => import("react-simplemde-editor").then((mod) => mod.default),
    { ssr: false }
);





export default function CreateArticle(props) {
    const router = useRouter()
    const [article, setArticle] = useState({
        title: '',
        subtitle: '',
        body: '',
        image: null,
        categories: []
    });
    const {I18n} = useAppContext()

    const [filename, setFilename] = useState(I18n("createArticle.selectFile"))

    const handleChange = (e) => {
        const el = e.target
        if (el.type === 'checkbox') {
            if (el.checked) {
                const aux = article.categories
                aux.push(el.value)
                setArticle({...article, categories: aux})
            } else {
                setArticle({...article, categories: article.categories.filter(c => c !== el.value)})
            }
        } else {
            setArticle({...article, [el.name]: el.type === "file" ? el.files[0] : el.value})
        }

        if (el.type === 'file')
            setFilename(el.files[0].name)
    }

    const categories = [
        { text: I18n("categories.tourism"), value: "categories.tourism" },
        { text: I18n("categories.entertainment"),value: "categories.entertainment" },
        { text: I18n("categories.politics"), value: "categories.politics" },
        { text: I18n("categories.economics"), value: "categories.economics" },
        { text: I18n("categories.sports"), value: "categories.sports"},
        { text: I18n("categories.technology"), value: "categories.technology" }
    ]

    return <div>
        <ModalTrigger modalId="closeModal">
            <BackButton></BackButton>
        </ModalTrigger>
        <div className="d-flex flex-col align-items-center justify-content-center p-5">


            <div id="custom-form-group" className="h-auto w-50">

                <div>
                    <label htmlFor="title">{I18n("createArticle.title")}</label>
                    <div className="form-group">
                        <div className="input-group mb-3">

                            <input id="title" name="title" className="form-control "
                                   placeholder={I18n("createArticle.title.placeholder")} type="text" value={article.title} onChange={handleChange}/>

                        </div>
                    </div>
                </div>


                <div>
                    <label htmlFor="subtitle">{I18n("createArticle.description")}</label>
                    <div className="form-group">
                        <div className="input-group mb-3">

                            <input onChange={handleChange} id="subtitle" name="subtitle" className="form-control "
                                   placeholder={I18n("createArticle.description.placeholder")} type="text" value={article.subtitle}/>
                                {/*<p id="subtitle.errors" className="invalid-feedback">The field cannot be empty</p>*/}
                        </div>
                    </div>
                </div>


                <div>
                    <label htmlFor="body">{I18n("createArticle.body")}</label>
                    <div className="form-group" data-color-mode="dark">

                        <div className="wmde-markdown-var"> </div>
                        <MarkdownEditor value={article.body} onChange={(text) => setArticle({...article, body: text})}/>
                    </div>

                </div>

                <label htmlFor="image">{I18n("createArticle.imageMsg")} </label>
                <div className="input-group mb-3">
                    <div className="custom-file">
                        {/*usar clases is-valid e is-invalid*/}
                        <input id="fileInput" name="image" className="custom-file-input" type="file"
                               accept="image/png, image/jpeg" onChange={handleChange}/>
                            <label htmlFor="inputGroupFile01" className="custom-file-label custom-file-input-label">{filename}</label>
                    </div>
                </div>

                <div className="dropdown" id="categories-dropdown">
                    <button className="btn btn-primary dropdown-toggle" type="button" id="dropdownMenuButton"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        {I18n("createArticle.category.choose")}
                    </button>
                    <div className="dropdown-menu bg-dropdown" aria-labelledby="dropdownMenuButton">

                        {categories.map(c => <div key={c.value} className="form-check  w-100">
                            <input onChange={handleChange} checked={!!(article.categories.find(cat => cat === c.value))} className="mr-1" id="categories.tourism" name="categories" type="checkbox" value={c.value}/>
                            <label className="form-check-label text-white" htmlFor={c.value}>
                                {c.text}
                            </label>
                        </div>)}



                    </div>
                </div>

                <div className="w-100 d-flex justify-content-end">
                    <button className="btn btn-info" type="submit">{I18n("createArticle.save")}</button>
                </div>
            </div>

            <Modal id="closeModal" body={I18n("createArticle.modal.msg")} title={I18n("createArticle.modal.question")} onClickHandler={(e) => router.back()}></Modal>

        </div>
    </div>
}