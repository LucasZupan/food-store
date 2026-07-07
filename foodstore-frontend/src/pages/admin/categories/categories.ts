import {
  getCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from "../../../services/category.service";

import type { ICategoria } from "../../../types/category";
import { checkAuthUser, logout } from "../../../utils/auth";
import { showToast } from "../../../utils/toast";

const form = document.getElementById("categoryForm") as HTMLFormElement;
const inputNombre = document.getElementById("nombre") as HTMLInputElement;
const inputDescripcion = document.getElementById("descripcion") as HTMLTextAreaElement;
const tableBody = document.getElementById("categoriesTableBody") as HTMLTableSectionElement;

const modal = document.getElementById("categoryModal") as HTMLDivElement;
const modalTitle = document.getElementById("modalTitle") as HTMLHeadingElement;
const openModalButton = document.getElementById("openModalButton") as HTMLButtonElement;
const closeModalButton = document.getElementById("closeModalButton") as HTMLButtonElement;
const logoutButton = document.getElementById("logoutButton") as HTMLButtonElement;

let editingCategoryId: number | null = null;
let categories: ICategoria[] = [];

const openModal = (): void => {
  modal.classList.add("open");
};

const closeModal = (): void => {
  modal.classList.remove("open");
};

const resetForm = (): void => {
  form.reset();
  editingCategoryId = null;
  modalTitle.textContent = "Nueva Categoría";
};

const fillFormForEdit = (category: ICategoria): void => {
  editingCategoryId = category.id;

  inputNombre.value = category.nombre;
  inputDescripcion.value = category.descripcion ?? "";
  modalTitle.textContent = "Editar Categoría";

  openModal();
};

const renderCategories = async (): Promise<void> => {
  try {
    categories = await getCategories();

    tableBody.innerHTML = "";

    if (categories.length === 0) {
      tableBody.innerHTML = `
        <tr>
          <td colspan="4">No hay categorías cargadas.</td>
        </tr>
      `;
      return;
    }

    categories.forEach((category) => {
      const row = document.createElement("tr");

      row.innerHTML = `
        <td>${category.id}</td>
        <td>${category.nombre}</td>
        <td>${category.descripcion}</td>
        <td>
          <button
            class="admin-action-button admin-edit-button"
            data-action="edit"
            data-id="${category.id}"
          >
            Editar
          </button>

          <button
            class="admin-action-button admin-delete-button"
            data-action="delete"
            data-id="${category.id}"
          >
            Eliminar
          </button>
        </td>
      `;

      tableBody.appendChild(row);
    });

    document.querySelectorAll<HTMLButtonElement>("[data-action]").forEach((button) => {
      button.addEventListener("click", async () => {
        const id = Number(button.dataset.id);
        const action = button.dataset.action;

        if (action === "edit") {
          const category = categories.find((item) => item.id === id);

          if (!category) {
            showToast("Categoría no encontrada", "error");
            return;
          }

          fillFormForEdit(category);
        }

        if (action === "delete") {
          try {
            await deleteCategory(id);
            showToast("Categoría eliminada", "success");
            await renderCategories();
          } catch (error) {
            const message =
              error instanceof Error ? error.message : "No se pudo eliminar";

            showToast(message, "error");
          }
        }
      });
    });
  } catch {
    showToast("Error cargando categorías", "error");
  }
};

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const data = {
    nombre: inputNombre.value.trim(),
    descripcion: inputDescripcion.value.trim(),
  };

  try {
    if (editingCategoryId) {
      await updateCategory(editingCategoryId, data);
      showToast("Categoría actualizada", "success");
    } else {
      await createCategory(data);
      showToast("Categoría creada", "success");
    }

    resetForm();
    closeModal();
    await renderCategories();
  } catch (error) {
    const message =
      error instanceof Error ? error.message : "No se pudo guardar la categoría";

    showToast(message, "error");
  }
});

openModalButton.addEventListener("click", () => {
  resetForm();
  openModal();
});

closeModalButton.addEventListener("click", () => {
  resetForm();
  closeModal();
});

modal.addEventListener("click", (e) => {
  if (e.target === modal) {
    resetForm();
    closeModal();
  }
});

const initPage = (): void => {
  checkAuthUser(
    "/src/pages/auth/login/login.html",
    "/src/pages/store/home/home.html",
    "ADMIN"
  );

  logoutButton.addEventListener("click", logout);

  renderCategories();
};

initPage();